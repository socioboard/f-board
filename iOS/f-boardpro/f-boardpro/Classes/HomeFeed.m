//
//  UnfollowViewController.m
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "HomeFeed.h"
#import "SingletonClass.h"
#import "SUCache.h"
#import "PAPPhotoDetailsViewController.h"
#import "UIImageView+WebCache.h"

@interface HomeFeed ()

@end

@implementation HomeFeed

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    [self fetchFeeds:nil];
    // Do any additional setup after loading the view.
}

-(void)viewWillAppear:(BOOL)animated{

    self.navigationController.navigationBarHidden=YES;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)fetchFeeds:(NSString *)accessToken{
    
    
    self.pictureArray = [[NSMutableArray alloc]init];
    self.storyArray = [[NSMutableArray alloc]init];
    self.descArray = [[NSMutableArray alloc]init];
    self.likeArray = [[NSMutableArray alloc]init];
    self.commentArray = [[NSMutableArray alloc]init];
    self.grpIdArray=[[NSMutableArray alloc] init];

    
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//    NSLog(@"index path %ld",index.section+index.row);
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/home" parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            
 self.pagingString = [[result objectForKey:@"paging"]objectForKey:@"next"];
        
      //  NSLog(@"result is %@",result);
       
        for (NSDictionary *dic in [result objectForKey:@"data"] ) {
            NSString *str1 = [dic objectForKey:@"picture"];
            NSString *str2 = [dic objectForKey:@"story"];
            NSString *str3 = [dic objectForKey:@"description"];
              NSString *str5 = [dic objectForKey:@"id"];
            NSString *str4 = [dic objectForKey:@"like_count"];
            
                if (str1) {
                [self.pictureArray addObject:str1];
                }else{
                [self.pictureArray addObject:@"nil"];
            }
            
            if (str5) {
                [self.grpIdArray addObject:str5];
            }else{
                [self.grpIdArray addObject:@"nil"];
            }

            
            if (str2) {
                [self.storyArray addObject:str2];
            }else{
                [self.storyArray addObject:@"nil"];
            }
            
            
                if (str3) {
                [self.descArray addObject:str3];
            }else{
                [self.descArray addObject:@"nil"];
            }
            
            if (str4) {
                [self.likeArray addObject:str4];
            }else{
                [self.likeArray addObject:@"nil"];
            }

            
        }
        NSLog(@"like array is %@",self.likeArray);
        
        [self createFollowTable];
        }];
    }
}



-(void)createFollowTable
{
    homeFeedTableView=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width,self.view.frame.size.height) style:UITableViewStylePlain];
    homeFeedTableView.dataSource=self;
    homeFeedTableView.delegate=self;
    homeFeedTableView.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:homeFeedTableView];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Follow";
    
    TableCustomCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    
    
    if (cell == nil)
    {
        cell = [[TableCustomCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    //    cell.backgroundColor= [UIColor redColor];
    
   NSString *picUrl =[self.pictureArray objectAtIndex:indexPath.row];
    
    
        if (![picUrl isEqualToString:@"nil"]) {
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        
    }else{
      [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *str1 =[self.storyArray objectAtIndex:indexPath.row];
    NSString *str2= [self.descArray objectAtIndex:indexPath.row];
    NSString *str3=[self.likeArray objectAtIndex:indexPath.row];
    
    if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
    }else{
        cell.userNameDesc.text=str2;
    }
    
    if ([str1  isEqualToString:@"nil"]) {
        if ([str2 isEqualToString:@"nil"]) {
            cell.userNameDesc.text=@"Posted something via Facebook";
        }
        
    }
    
    if (![str3 isEqualToString:@"nil"]) {
        cell.likeCountLabel.text=str1;
    }else{
        cell.likeCountLabel.text=@"0";
    }
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//    NSLog(@"index path %ld",index.section+index.row);
    NSString *str1 =[self.grpIdArray objectAtIndex:indexPath.row];
    
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",str1] parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
            
          
            PAPPhotoDetailsViewController *photoDetail=[[PAPPhotoDetailsViewController alloc] initWithDict:result];
            self.navigationController.navigationBarHidden=NO;
            [self.navigationController pushViewController:photoDetail animated:YES];
            
            
        }];
        
    }
}
-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.pictureArray.count;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 140;
}

-(void) tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSLog(@"index is %ld",(long)indexPath.row);
    NSLog(@"self.feeds array count is %lu",(unsigned long)self.pictureArray.count);
    
    if (indexPath.row == self.pictureArray.count-1) {
            // [self retrieveMoreObjects];
    }
    
}

-(void) retrieveMoreObjects{
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    
    NSString *accessToken1=[[NSUserDefaults standardUserDefaults]objectForKey:@"accessToken"];
    NSIndexPath *userIndex=[SingletonClass sharedState].selectedUserIndex;
//    NSInteger ind =0;
    FBSDKAccessToken *token = [SUCache itemForSlot:userIndex.row+userIndex.section].token;
    
    NSString *str1 =self.pagingString;
    
    NSString * urlStr=str1;
    
    NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSURL *url=[NSURL URLWithString:urlStr2];
    
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    NSLog(@"reponse is %@",response);
    
    self.pagingString = [[response objectForKey:@"paging"]objectForKey:@"next"];
    NSString *pageStr = [[response objectForKey:@"paging"]objectForKey:@"previous"];
    if (self.pagingString) {
            // NSLog(@"self.pagestr is %@",self.pagingString);
    }
    
    
    for (NSDictionary *dic in [response objectForKey:@"data"] ) {
        NSString *str = [dic objectForKey:@"picture"];
        NSString *str2 = [dic objectForKey:@"story"];
        NSString *str3 = [dic objectForKey:@"description"];
        NSString *str5 = [dic objectForKey:@"id"];
        NSString *str4 = [dic objectForKey:@"like_count"];
        
        if (str) {
            [self.pictureArray addObject:str1];
        }else{
            [self.pictureArray addObject:@"nil"];
        }
        
        if (str5) {
            [self.grpIdArray addObject:str5];
        }else{
            [self.grpIdArray addObject:@"nil"];
        }
        
        
        if (str2) {
            [self.storyArray addObject:str2];
        }else{
            [self.storyArray addObject:@"nil"];
        }
        
        
        if (str3) {
            [self.descArray addObject:str3];
        }else{
            [self.descArray addObject:@"nil"];
        }
        
        if (str4) {
            [self.likeArray addObject:str4];
        }else{
            [self.likeArray addObject:@"nil"];
        }
        
        
    }
//    NSLog(@"self.feeds array is %@",self.feedsArray);
    [homeFeedTableView reloadData];
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
